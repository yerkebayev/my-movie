package mymovie.jobs;

import mymovie.model.*;
import mymovie.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@EnableBatchProcessing
@Configuration
public class PopulateMongo {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final MongoTemplate mongoTemplate;

    public PopulateMongo(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MongoTemplate mongoTemplate) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public Job populate() throws Exception {
        return jobBuilderFactory.get("populate").incrementer(new RunIdIncrementer()).start(step1())
            .next(step2()).next(step3()).next(step4()).next(step5()).build();
    }

    @Bean
    public Step step1() throws Exception {
        mongoTemplate.dropCollection("movie");
        return stepBuilderFactory.get("step1").<Movie, Movie>chunk(10).reader(movieReader())
                .writer(movieWriter()).build();
    }

    @Bean
    public Step step2() throws Exception {
        mongoTemplate.dropCollection("rating");
        return stepBuilderFactory.get("step2").<Rating, Rating>chunk(10).reader(ratingReader())
                .writer(ratingWriter()).build();
    }

    @Bean
    public Step step3() throws Exception {
        mongoTemplate.dropCollection("user");
        return stepBuilderFactory.get("step3").<User, User>chunk(10).reader(userReader())
                .writer(userWriter()).build();
    }

    @Bean
    public Step step4() throws Exception {
        mongoTemplate.dropCollection("imdb");
        return stepBuilderFactory.get("step4").<Imdb, Imdb>chunk(10).reader(imdbReader())
                .writer(imdbWriter()).build();
    }

    @Bean
    public Step step5() throws Exception {
        mongoTemplate.dropCollection("thumbnail");
        return stepBuilderFactory.get("step5").<Thumbnail, Thumbnail>chunk(10).reader(thumbnailReader())
                .writer(thumbnailWriter()).build();
    }

    @Bean
    public FlatFileItemReader<Movie> movieReader() throws Exception {
        FlatFileItemReader<Movie> reader = new FlatFileItemReader<>();
        reader.setResource(new PathResource(this.getClass().getResource( "/data/movies.dat" ).toURI()));
        reader.setLineMapper(new DefaultLineMapper<Movie>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter("::");
                setNames(new String[]{"movieId", "title", "genres"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Movie>() {{
                setTargetType(Movie.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FlatFileItemReader<Rating> ratingReader() throws Exception {
        FlatFileItemReader<Rating> reader = new FlatFileItemReader<>();
        reader.setResource(new PathResource(this.getClass().getResource( "/data/ratings.csv" ).toURI()));
        reader.setLineMapper(new DefaultLineMapper<Rating>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"userId", "movieId", "rating", "timestamp"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Rating>() {{
                setTargetType(Rating.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FlatFileItemReader<User> userReader() throws Exception {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new PathResource(this.getClass().getResource( "/data/users.csv" ).toURI()));
        reader.setLineMapper(new DefaultLineMapper<User>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"userId", "gender", "age", "occupation", "zipCode"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                setTargetType(User.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FlatFileItemReader<Imdb> imdbReader() throws Exception {
        FlatFileItemReader<Imdb> reader = new FlatFileItemReader<>();
        reader.setResource(new PathResource(this.getClass().getResource( "/data/links.csv" ).toURI()));
        reader.setLineMapper(new DefaultLineMapper<Imdb>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"movieId", "imdb"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Imdb>() {{
                setTargetType(Imdb.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FlatFileItemReader<Thumbnail> thumbnailReader() throws Exception {
        FlatFileItemReader<Thumbnail> reader = new FlatFileItemReader<>();
        reader.setResource(new PathResource(this.getClass().getResource( "/data/movie_poster.csv" ).toURI()));
        reader.setLineMapper(new DefaultLineMapper<Thumbnail>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"movieId", "thumbnail"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Thumbnail>() {{
                setTargetType(Thumbnail.class);
            }});
        }});
        return reader;
    }

    @Bean
    public MongoItemWriter<Movie> movieWriter() {
        MongoItemWriter<Movie> writer = new MongoItemWriter<Movie>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("movie");
        return writer;
    }

    @Bean
    public MongoItemWriter<Rating> ratingWriter() {
        MongoItemWriter<Rating> writer = new MongoItemWriter<Rating>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("rating");
        return writer;
    }

    @Bean
    public MongoItemWriter<User> userWriter() {
        MongoItemWriter<User> writer = new MongoItemWriter<User>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("user");
        return writer;
    }

    @Bean
    public MongoItemWriter<Imdb> imdbWriter() {
        MongoItemWriter<Imdb> writer = new MongoItemWriter<Imdb>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("imdb");
        return writer;
    }

    @Bean
    public MongoItemWriter<Thumbnail> thumbnailWriter() {
        MongoItemWriter<Thumbnail> writer = new MongoItemWriter<Thumbnail>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("thumbnail");
        return writer;
    }
}