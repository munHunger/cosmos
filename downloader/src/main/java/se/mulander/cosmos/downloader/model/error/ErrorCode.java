package se.tfmoney.microservice.tfMCCMicro.model.error;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by Marcus Münger on 2017-04-27.
 */
@XmlRootElement(name = "ErrorCode")
@Deprecated
public class ErrorCode
{
    @XmlElement(name = "error_code")
    @SerializedName("error_code")
    public String errorCode;

    @XmlElement(name = "error_messages")
    @SerializedName("error_messages")
    public Map<String, ErrorMessage> errorMessages;
}
