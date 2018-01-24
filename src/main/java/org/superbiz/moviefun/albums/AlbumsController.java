package org.superbiz.moviefun.albums;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.superbiz.moviefun.dao.Blob;
import org.superbiz.moviefun.dao.BlobStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RequestMapping("/albums")
public class AlbumsController {

    private BlobStore blobStore;
    private final AlbumsBean albumsBean;

    public AlbumsController(AlbumsBean albumsBean, BlobStore blobStore) {
        this.albumsBean = albumsBean;
        this.blobStore = blobStore;
    }


    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("albums", albumsBean.getAlbums());
        return "albums";
    }

    @GetMapping("/{albumId}")
    public String details(@PathVariable long albumId, Map<String, Object> model) {
        model.put("album", albumsBean.find(albumId));
        return "albumDetails";
    }

    @PostMapping("/{albumId}/cover")
    public String uploadCover(@PathVariable long albumId, @RequestParam("file") MultipartFile uploadedFile) throws IOException {
        //blobStore.put(new Blob(String.valueOf(albumId), uploadedFile.getInputStream(), uploadedFile.getContentType()));
        blobStore.put(new Blob(getCoverName(albumId), uploadedFile.getInputStream(), uploadedFile.getContentType()));
        //blobStore.put(new Blob("cover/" + albumId), uploadedFile.getInputStream(), uploadedFile.getContentType()));

        return format("redirect:/albums/%d", albumId);
    }

    private String getCoverName(long albumId) {
        return format("covers/%d", albumId);
    }

    @GetMapping("/{albumId}/cover")
    public HttpEntity<byte[]> getCover(@PathVariable long albumId) throws IOException, URISyntaxException {

        Optional<Blob> blob =  blobStore.get(getCoverName(albumId));

        HttpHeaders headers = new HttpHeaders();
        byte[] imageBytes = null;
        if(blob.isPresent()) {
            Blob blobObj = blob.get();
            headers.setContentType(MediaType.parseMediaType(blobObj.getContentType()));

            imageBytes = IOUtils.toByteArray(blobObj.getInputStream());
            headers.setContentLength(imageBytes.length);
        }
        return new HttpEntity<>(imageBytes, headers);
    }

}
