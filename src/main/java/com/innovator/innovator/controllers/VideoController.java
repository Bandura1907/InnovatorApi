//package com.innovator.innovator.controllers;
//
//import com.innovator.innovator.services.NewsService;
//import com.innovator.innovator.services.StreamBytesInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpRange;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//public class VideoController {
//
//    private final NewsService newsService;
//
//    @Autowired
//    public VideoController(NewsService newsService) {
//        this.newsService = newsService;
//    }
//
//    @GetMapping("/stream/{id}")
//    public ResponseEntity<StreamingResponseBody> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeHeader,
//                                                             @PathVariable("id") Integer id) {
//        List<HttpRange> httpRangeList = HttpRange.parseRanges(httpRangeHeader);
//        StreamBytesInfo streamBytesInfo = newsService.getStreamBytesInfo(id, httpRangeList.size() > 0 ? httpRangeList.get(0) : null)
//                .orElseThrow(NotFoundException::new);
//        long byteLength = streamBytesInfo.getRangeEnd() - streamBytesInfo.getRangeStart() + 1;
//        ResponseEntity.BodyBuilder builder = ResponseEntity.status(httpRangeList.size() > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
//                .header("Content-Type", streamBytesInfo.getContentType())
//                .header("Accept-Ranges", "bytes")
//                .header("Content-Length", Long.toString(byteLength));
//
//        if (httpRangeList.size() > 0) {
//            builder.header("Content-Range",
//                    "bytes " + streamBytesInfo.getRangeStart() +
//                            "-" + streamBytesInfo.getRangeEnd() +
//                            "/" + streamBytesInfo.getFileSize());
//        }
//
//        return builder.body(streamBytesInfo.getResponseBody());
//    }
//}
