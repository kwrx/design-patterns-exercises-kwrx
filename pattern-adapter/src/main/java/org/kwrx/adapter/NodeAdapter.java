/*
 * MIT License
 *
 * Copyright (c) 2020 Antonino Natale
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.kwrx.adapter;

import java.nio.file.Files;
import java.nio.file.Path;

public class NodeAdapter {

    private final Path path;

    public NodeAdapter(Path path) {
        this.path = path;
    }

    public String getIcon() {

        if(getType() == NodeType.DIRECTORY)
            return "FOLDER";

        if(getName().lastIndexOf('.') < 0)
            return "FILE";


        var names = getName().split("\\.");

        return switch (names[names.length - 1].toLowerCase()) {
            case "avi", "mp4" -> "FILE_VIDEO";
            case "bmp", "gif", "jpeg", "jpg", "png", "svg", "tiff", "webp" -> "FILE_IMAGE";
            case "bz", "bz2", "gz", "rar", "zip", "7z" -> "ARCHIVE";
            case "xml", "html", "php", "sh" -> "FILE_XML";
            case "csv", "xls", "xlsx" -> "FILE_EXCEL";
            case "doc", "docx", "epub" -> "FILE_WORD";
            case "jar" -> "APPLICATION";
            case "js" -> "FILE_LANGUAGE_JAVASCRIPT";
            case "json" -> "JSON";
            case "pdf" -> "FILE_PDF";
            case "ppt", "pptx" -> "FILE_POWERPOINT";
            case "tar" -> "FILE_ARCHIVE";
            case "wav", "mp3" -> "FILE_MUSIC";
            default -> "FILE";
        };

    }

    public String getName() {
        return path.getFileName().toString();
    }

    public NodeType getType() {
        return Files.isDirectory(path)
                ? NodeType.DIRECTORY
                : NodeType.FILE;
    }

}
