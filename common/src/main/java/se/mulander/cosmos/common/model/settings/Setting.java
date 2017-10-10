package se.mulander.cosmos.common.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by marcu on 2017-07-01.
 */
@Entity
@Table(name = "setting")
@ApiModel(description = "A setting object that can be a root or part of a tree structure of settings")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Setting {
    @Id
    @Column(name = "id", length = 64)
    @ApiModelProperty(value = "The globally unique identifier for this setting")
    public String internalID;
    @ApiModelProperty(value = "The identifier of this setting. Note that this might not be the full identifier as the" +
            " setting might have a parent that makes up parts of the identifier")
    @Column(name = "name")
    public String name;
    @ApiModelProperty(value = "The type of setting. This will help determine how to parse inputs",
                      allowableValues = "string, number, boolean, group")
    @Column(name = "type", length = 7)
    public String type;
    @ApiModelProperty(value = "A regex to evaluate string settings. Note that this only applies to settings where " +
            "type=string. Backend must accept any string that matches regex, although no guarantee of correctness")
    @Column(name = "regex", length = 512)
    public String regex;

    @ManyToOne(optional = true)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    public Setting parent;

    @ApiModelProperty(dataType = "java.util.ArrayList")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = {CascadeType.ALL})
    public Collection<Setting> children;

    @ApiModelProperty(value = "The setting value. This is subject to change during the applications lifecycle")
    @Column(name = "value", length = 512)
    public String value;

    public Setting() {
        this.internalID = UUID.randomUUID().toString();
    }

    public Setting(String name, Setting... children) {
        this();
        this.name = name;
        this.type = "group";
        this.children = Arrays.asList(children);
    }

    public Setting(String name, String regex, String value) {
        this();
        this.name = name;
        this.regex = regex;
        this.type = "string";
        this.value = value;
    }
}
