package big.data.bigdata.controller;

import htsjdk.samtools.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/igv")
public class IgvController {
    @GetMapping("/bam")
    public void getBamRegion(
            @RequestParam String file,
            @RequestParam String chr,
            @RequestParam int start,
            @RequestParam int end,
            HttpServletResponse response) throws IOException{

        //1.设置响应头（IGV需要的格式）
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename = " + file);
        // 2. 使用htsjdk读取BAM区域
        SamReader reader = SamReaderFactory.makeDefault()
                .open(new File("/path/to/bam/files/" + file));

        SAMRecordIterator iterator = reader.query(chr, start, end, true);

        // 3. 流式传输记录
        OutputStream out = response.getOutputStream();
        while (iterator.hasNext()) {
            SAMRecord record = iterator.next();
            SAMFileHeader header = reader.getFileHeader();
            SAMFileWriterFactory factory = new SAMFileWriterFactory();
            SAMFileWriter writer = factory.makeBAMWriter(header, true, response.getOutputStream());

            while (iterator.hasNext()) {
                writer.addAlignment(iterator.next());
            }

            writer.close();
    }
        iterator.close();
        reader.close();
    }
}
