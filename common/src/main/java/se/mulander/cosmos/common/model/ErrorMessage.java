package se.mulander.cosmos.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by marcu on 2017-07-01.
 */
@ApiModel(description = "A code/message pair describing what went wrong")
public class ErrorMessage
{
	@ApiModelProperty(value = "The code describing what went wrong", example = "Error during division")
	public String code;
	@ApiModelProperty(value = "A text describing why it went wrong", example = "Divisor was 0")
	public String message;

	public ErrorMessage()
	{
	}

	public ErrorMessage(String code, String message)
	{
		this.code = code;
		this.message = message;
	}
}