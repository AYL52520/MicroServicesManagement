package com.micro.service.constant;

import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

/**
 * @Classname InMemoryResource
 * 上传文件的类必须使用 FileSystemResource。有时我们会碰到这种情况：
 * 文件我们会从文件服务下载到内存中一个 InputStream 的形式存在，那此时在使用 FileSystemResource 就不行了。
 * 写在内存里 上传文件就可以不用写了
 * @Version 1.0.0
 * @Date 2023/2/5 16:12
 * @Created by yangle
 */
// 自己实现了一个 Resource
public class InMemoryResource extends ByteArrayResource {
    private final String filename;
    private final long lastModified;

    public InMemoryResource(String filename, String description, byte[] content, long lastModified) {
        super(content, description);
        this.lastModified = lastModified;
        this.filename = filename;
    }

    @Override
    public long lastModified() throws IOException {
        return this.lastModified;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }
}
