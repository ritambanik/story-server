package com.example.story.gateway;

import com.example.story.data.Story;
import com.example.story.data.StoryForm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static com.example.story.data.Story.createStoryBuilder;

@Repository
@ConditionalOnProperty(value = "env.test", matchIfMissing = true)
public class MySqlStoryRepository implements StoryRepository {

    private JdbcTemplate jdbcTemplate;
    private KeyHolder keyHolder = new GeneratedKeyHolder();

    public MySqlStoryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Story createStory(StoryForm storyForm) {
        jdbcTemplate.update(con ->
             {
                 PreparedStatement ps =  con.prepareStatement("INSERT INTO story (projectId, name, info) VALUES (?,?,?)", RETURN_GENERATED_KEYS);
                 ps.setInt(1, storyForm.getProjectId());
                 ps.setString(2, storyForm.getName());
                 ps.setString(3, storyForm.getInfo());
                 return ps;
             }, keyHolder);
        int id = keyHolder.getKey().intValue();
        return jdbcTemplate.queryForObject("SELECT id, projectId, name, info FROM story WHERE id = " + id, rowMapper);
    }

    @Override
    public Optional<Story> fetchStory(Integer id) {
        try{
            return Optional.of(jdbcTemplate.queryForObject("SELECT id, projectId, name, info FROM story WHERE id = " + id, rowMapper));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Story> fetchAllStories() {
        return jdbcTemplate.query("SELECT id, projectId, name, info FROM story ", rowMapper);
    }

    @Override
    public Optional<Story> updateStory(Integer id, StoryForm story) {
        jdbcTemplate.update(con -> {
           PreparedStatement ps = con.prepareStatement("UPDATE story SET projectId = ?, name = ?, info = ? WHERE id = ?");
           ps.setInt(1, story.getProjectId());
           ps.setString(2, story.getName());
           ps.setString(3, story.getInfo());
           ps.setInt(4, id);
           return ps;
        });
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT id, projectId, name, info FROM story WHERE id = " + id, rowMapper));
    }

    @Override
    public Optional<Boolean> deleteStory(Integer id) {
        int count = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("DELETE FROM story WHERE id = ?");
            ps.setInt(1, id);
            return ps;
        });
        return Optional.of(count > 0 ? Boolean.TRUE : Boolean.FALSE);
    }

    private RowMapper<Story> rowMapper = ((rs, rowNum) -> createStoryBuilder().id(rs.getInt("id"))
            .projectId(rs.getInt("projectId")).name(rs.getString("name")).info(rs.getString("info")).build());
}
