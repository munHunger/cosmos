package se.mulander.cosmos.settings.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by marcu on 2017-07-01.
 */
@Entity
@Table(name = "setting")
@ApiModel(value = "A setting object that can be a root or part of a tree structure of settings")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Setting
{
	@Id
	@Column(name = "id")
	@ApiModelProperty(value = "The globally unique identifier for this setting")
	public String internalID;
	@ApiModelProperty(value = "The identifier of this setting. Note that this might not be the full identifier as the setting might have a parent that makes up parts of the identifier")
	@Column(name = "name")
	public String name;
	@ApiModelProperty(value = "The type of setting. This will help determine how to parse inputs",
					  allowableValues = "string, number, boolean, group")
	@Column(name = "type")
	public String type;
	@ApiModelProperty(value = "A regex to evaluate string settings. Note that this only applies to settings where type=string. Backend must accept any string that matches regex, although no guarantee of correctness")
	@Column(name = "regex")
	public String regex;

	@ApiModelProperty(value = "A group of child settings. This only applies to settings where type=group")
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "setting",
			   joinColumns = {@JoinColumn(name = "parent_id")},
			   inverseJoinColumns = {@JoinColumn(name = "id")})
	public Collection<Setting> children;

	@JsonIgnore
	@Column(name = "parent_id")
	public String parentID;

	@ApiModelProperty(value = "The setting value. This is subject to change during the applications lifecycle")
	@Column(name = "value")
	public String value;

	public Setting()
	{
		this.internalID = UUID.randomUUID().toString();
	}
}
