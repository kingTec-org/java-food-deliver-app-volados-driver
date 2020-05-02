package gofereatsdriver.interfaces;

/**
 * @package com.trioangle.gofereatsdriver
 * @subpackage interfaces
 * @category ServiceListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import gofereatsdriver.datamodels.main.JsonResponse;

/*****************************************************************
 ServiceListener
 ****************************************************************/
public interface ServiceListener {

    void onSuccess(JsonResponse jsonResp, String data);

    void onFailure(JsonResponse jsonResp, String data);
}
