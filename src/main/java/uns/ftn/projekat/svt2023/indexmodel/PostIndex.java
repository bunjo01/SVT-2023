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
@Document(indexName = "posts")
public class PostIndex {

    @Id
    private String id;

    @Field(type = FieldType.Integer, store = true, name = "database_id")
    private Integer databaseId;

    @Field(type = FieldType.Integer, store = true, name = "group_id")
    private Integer groupId;

    @Field(type = FieldType.Text, store = true, name = "title", analyzer = "serbian")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "content", analyzer = "serbian")
    private String content;

    @Field(type = FieldType.Text, store = true, name = "pdf_description_url")
    private String pdfDescriptionUrl;

    @Field(type = FieldType.Text, store = true, name = "pdf_text", analyzer = "serbian")
    private String pdfText;
}
