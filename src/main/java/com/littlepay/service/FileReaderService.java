package com.littlepay.service;

import com.littlepay.model.InputRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Abstract service class for reading files.
 *
 * @author Sachi
 */
@Service
public abstract class FileReaderService {

    /**
     * Read a file.
     *
     * @throws IOException if an I/O error occurs while processing the file
     */
    public abstract List<InputRecord> process(String fileName) throws IOException;
}
