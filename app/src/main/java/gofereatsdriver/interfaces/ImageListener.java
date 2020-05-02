package gofereatsdriver.interfaces;
/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage interfaces
 * @category ImageListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import okhttp3.RequestBody;

/*****************************************************************
 ImageListener
 ****************************************************************/

public interface ImageListener {
    void onImageCompress(String filePath, RequestBody requestBody);
}
