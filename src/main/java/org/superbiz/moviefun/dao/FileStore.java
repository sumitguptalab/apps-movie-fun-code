package org.superbiz.moviefun.dao;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;


public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.getName());
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(blob.getInputStream(), outputStream);
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        File file = new File(name);
        Optional<Blob> blob = Optional.empty();
        if (file.exists()) {
            blob = Optional.of(new Blob(name, new FileInputStream(file), new Tika().detect(file)));
        }

        return blob;
    }

    @Override
    public void deleteAll() {
    }


}
