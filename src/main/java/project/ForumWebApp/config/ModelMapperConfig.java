package project.ForumWebApp.config;

import java.util.Set;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.Like;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.models.DTOs.PostSummaryDTO;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);


        Converter<Set<Like>, Integer> likesToLikeCount = context ->
                context.getSource() == null ? 0 : context.getSource().size();


        TypeMap<Comment, CommentDTO> commentToCommentDtoTypeMap = modelMapper.createTypeMap(Comment.class, CommentDTO.class);
        commentToCommentDtoTypeMap.addMappings(mapper -> mapper.map(src -> src.getPost().getId(), CommentDTO::setPostId));


        TypeMap<CommentDTO, Comment> commentDtoToCommentTypeMap =
                modelMapper.createTypeMap(CommentDTO.class, Comment.class);

        modelMapper.createTypeMap(Post.class, PostSummaryDTO.class)
                .addMappings(mapper -> mapper.using(likesToLikeCount).map(Post::getLikes, PostSummaryDTO::setLikeCount));

        return modelMapper;
    }
}
