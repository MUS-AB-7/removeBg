package in.musab.removebg.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import in.musab.removebg.service.RemoveBackgroundService;

@Service
public class RemoveBackgroundServiceImpl implements RemoveBackgroundService {

    private static final String CLIPDROP_REMOVE_BG_URL = "https://clipdrop-api.co/remove-background/v1";

    @Value("${clipdrop.apiKey}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    @Override
    public byte[] removeBackground(MultipartFile file) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Clipdrop API key is missing");
        }

        try {
            ByteArrayResource imageResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.parseMediaType(
                    file.getContentType() != null ? file.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image_file", new HttpEntity<>(imageResource, fileHeaders));

            return restClient.post()
                    .uri(CLIPDROP_REMOVE_BG_URL)
                    .header("x-api-key", apiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .body(body)
                    .retrieve()
                    .body(byte[].class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read uploaded image", e);
        } catch (RestClientException e) {
            throw new IllegalStateException("Clipdrop background removal request failed", e);
        }
    }

}
