package uns.ftn.projekat.svt2023.indexmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "groups")
public class GroupIndex {

    @Id
    private String id;

    @Field(type = FieldType.Integer, store = true, name = "database_id")
    private Integer databaseId;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian")
    private String description;

    @Field(type = FieldType.Text, store = true, name = "pdf_description_url")
    private String pdfDescriptionUrl;

    @Field(type = FieldType.Text, store = true, name = "pdf_text", analyzer = "serbian")
    private String pdfText;

    @Field(type = FieldType.Integer, store = true, name = "number_of_likes")
    private int numberOfLikes;

    @Field(type = FieldType.Integer, store = true, name = "number_of_posts")
    private int numberOfPosts;

    @Field(type = FieldType.Double, store = true, name = "average_likes")
    private double averageLikes;
}
