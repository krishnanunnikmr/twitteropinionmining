package twitter.opinion.mining.domain.service.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("storage")
public class StorageProperties {
    private String uploadedFileLocation="upload-dir";

    public String getUploadedFileLocation() {
        return uploadedFileLocation;
    }

    public void setUploadedFileLocation(String uploadedFileLocation) {
        this.uploadedFileLocation = uploadedFileLocation;
    }
}
