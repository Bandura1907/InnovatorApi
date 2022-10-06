package com.innovator.innovator.controllers;

import com.innovator.innovator.payload.request.NewVideoRequest;
import com.innovator.innovator.models.VideoMetadata;
import com.innovator.innovator.payload.response.MessageResponse;
import com.innovator.innovator.repository.VideoMetadataRepository;
import com.innovator.innovator.services.NewsService;
import com.innovator.innovator.services.VideoStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/video")
@Slf4j
public class VideoMetadataController {

    @Value("${upload.path.video}")
    private String uploadPathVideo;

//    private final VideoService videoService;
    private final VideoStreamService videoStreamService;
    private final VideoMetadataRepository videoMetadataRepository;

    @Autowired
    public VideoMetadataController(VideoStreamService videoStreamService, VideoMetadataRepository videoMetadataRepository) {
//        this.videoService = videoService;
        this.videoStreamService = videoStreamService;
        this.videoMetadataRepository = videoMetadataRepository;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<VideoMetadataRepr> findVideoMetadataById(@PathVariable Integer id) {
//        Optional<VideoMetadataRepr> videoMetadata = videoService.findById(id);
//        if (videoMetadata.isEmpty())
//            return ResponseEntity.notFound().build();
//
//        return new ResponseEntity<>(videoMetadata.get(), HttpStatus.OK);
//    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<?> streamVideo(@PathVariable int id, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Optional<VideoMetadata> videoMetadata = videoMetadataRepository.findById(id);
        if (videoMetadata.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Video not found"), HttpStatus.NOT_FOUND);

        File videoFile = new File(uploadPathVideo + videoMetadata.get().getFileName());
        request.setAttribute(NewsService.ATTR_FILE, videoFile);
        videoStreamService.handleRequest(request, response);

        return ResponseEntity.ok().build();
    }

//    @GetMapping("/stream/{id}")
//    public ResponseEntity<StreamingResponseBody> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeHeader,
//                                                             @PathVariable("id") Integer id) {
//        log.info("Requested range [{}] for file `{}`", httpRangeHeader, id);
//
//        List<HttpRange> httpRangeList = HttpRange.parseRanges(httpRangeHeader);
//        StreamBytesInfo streamBytesInfo = videoService.getStreamBytesInfo(id, httpRangeList.size() > 0 ? httpRangeList.get(0) : null)
//                .orElseThrow(NotFoundException::new);
//
//        long byteLength = streamBytesInfo.getRangeEnd() - streamBytesInfo.getRangeStart() + 1;
//        ResponseEntity.BodyBuilder builder = ResponseEntity.status(httpRangeList.size() > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
//                .header("Content-Type", streamBytesInfo.getContentType())
//                .header("Accept-Ranges", "bytes")
//                .header("Content-Length", Long.toString(byteLength));
//
//
//        if (httpRangeList.size() > 0) {
//            builder.header("Content-Range",
//                    "bytes " + streamBytesInfo.getRangeStart() +
//                            "-" + streamBytesInfo.getRangeEnd() +
//                            "/" + streamBytesInfo.getFileSize());
//        }
//
//        log.info("Providing bytes from {} to {}. We are at {}% of overall video.",
//                streamBytesInfo.getRangeStart(), streamBytesInfo.getRangeEnd(),
//                new DecimalFormat("###.##").format(100.0 * streamBytesInfo.getRangeStart() / streamBytesInfo.getFileSize()));
//        return builder.body(streamBytesInfo.getResponseBody());
//    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> uploadVideo(NewVideoRequest newVideoRequest) {

        VideoMetadata videoMetadata;
        try {
             videoMetadata = videoStreamService.saveNewVideo(newVideoRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(videoMetadata.getId());
    }
}
