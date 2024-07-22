package project.ForumWebApp.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.ForumWebApp.models.DTOs.PostSummaryDTO;
import project.ForumWebApp.models.Like;
import project.ForumWebApp.models.Post;

import java.util.Set;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);


        Converter<Set<Like>, Integer> likesToLikeCount = new Converter<Set<Like>, Integer>() {
            @Override
            public Integer convert(MappingContext<Set<Like>, Integer> context) {
                return context.getSource() == null ? 0 : context.getSource().size();
            }
        };

        modelMapper.createTypeMap(Post.class, PostSummaryDTO.class)
                .addMappings(mapper -> mapper.using(likesToLikeCount).map(Post::getLikes, PostSummaryDTO::setLikeCount));

        return modelMapper;
    }
}
