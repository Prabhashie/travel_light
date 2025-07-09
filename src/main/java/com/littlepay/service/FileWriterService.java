package com.littlepay.service;

import com.littlepay.model.OutputRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Abstract service class for writing to files.
 *
 * @author Sachi
 */
@Service
public abstract class FileWriterService {

    /**
     * Write to a file.
     *
     * @throws IOException if an I/O error occurs while processing the file
     */
    public abstract void process(String fileName, List<OutputRecord> outputRecords) throws IOException;
}
