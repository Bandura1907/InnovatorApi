package com.innovator.innovator.controllers;

import com.innovator.innovator.controllers.repr.NewVideoRepr;
import com.innovator.innovator.controllers.repr.VideoMetadataRepr;
import com.innovator.innovator.models.VideoMetadata;
import com.innovator.innovator.services.StreamBytesInfo;
import com.innovator.innovator.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
@Slf4j
public class VideoMetadataController {

    private final VideoService videoService;

    @Autowired
    public VideoMetadataController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoMetadataRepr> findVideoMetadataById(@PathVariable Integer id) {
        Optional<VideoMetadataRepr> videoMetadata = videoService.findById(id);
        if (videoMetadata.isEmpty())
            return ResponseEntity.notFound().build();

        return new ResponseEntity<>(videoMetadata.get(), HttpStatus.OK);
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<StreamingResponseBody> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeHeader,
                                                             @PathVariable("id") Integer id) {
        log.info("Requested range [{}] for file `{}`", httpRangeHeader, id);

        List<HttpRange> httpRangeList = HttpRange.parseRanges(httpRangeHeader);
        StreamBytesInfo streamBytesInfo = videoService.getStreamBytesInfo(id, httpRangeList.size() > 0 ? httpRangeList.get(0) : null)
                .orElseThrow(NotFoundException::new);

        long byteLength = streamBytesInfo.getRangeEnd() - streamBytesInfo.getRangeStart() + 1;
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(httpRangeList.size() > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .header("Content-Type", streamBytesInfo.getContentType())
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", Long.toString(byteLength));


        if (httpRangeList.size() > 0) {
            builder.header("Content-Range",
                    "bytes " + streamBytesInfo.getRangeStart() +
                            "-" + streamBytesInfo.getRangeEnd() +
                            "/" + streamBytesInfo.getFileSize());
        }

        log.info("Providing bytes from {} to {}. We are at {}% of overall video.",
                streamBytesInfo.getRangeStart(), streamBytesInfo.getRangeEnd(),
                new DecimalFormat("###.##").format(100.0 * streamBytesInfo.getRangeStart() / streamBytesInfo.getFileSize()));
        return builder.body(streamBytesInfo.getResponseBody());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> uploadVideo(NewVideoRepr newVideoRepr) {

        VideoMetadata videoMetadata;
        try {
             videoMetadata = videoService.saveNewVideo(newVideoRepr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(videoMetadata.getId());
    }
}
