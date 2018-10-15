/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.model

const val CUSTOM_STATUS_CODE_GET_ENDPOINT = "/get/custom-status-code"
const val SIMPLE_GET_ENDPOINT = "/get/simple"
const val QUERY_PARAM_GET_ENDPOINT = "/get/query-param"
const val SINGLE_PATH_PARAM_GET_ENDPOINT = "/get/path/{$PATH_PARAM_1}"
const val MULTI_PATH_PARAM_GET_ENDPOINT = "/get/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

const val SIMPLE_DELETE_ENDPOINT = "/delete/simple"
const val QUERY_PARAM_DELETE_ENDPOINT = "/delete/query-param"
const val SINGLE_PATH_PARAM_DELETE_ENDPOINT = "/delete/path/{$PATH_PARAM_1}"
const val MULTIPLE_PATH_PARAM_DELETE_ENDPOINT = "/delete/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

const val SIMPLE_PATCH_ENDPOINT = "/patch/simple"
const val SIMPLE_REQUEST_BODY_PATCH_ENDPOINT = "/patch/request-body"
const val QUERY_PARAM_PATCH_ENDPOINT = "/patch/query-param"
const val SINGLE_PATH_PARAM_PATCH_ENDPOINT = "/patch/path/{$PATH_PARAM_1}"
const val MULTI_PATH_PARAM_PATCH_ENDPOINT = "/patch/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

const val SIMPLE_POST_ENDPOINT = "/post/simple"
const val SIMPLE_REQUEST_BODY_POST_ENDPOINT = "/post/request-body"
const val QUERY_PARAM_POST_ENDPOINT = "/post/query-param"
const val SINGLE_PATH_PARAM_POST_ENDPOINT = "/post/path/{$PATH_PARAM_1}"
const val MULTI_PATH_PARAM_POST_ENDPOINT = "/post/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

const val SIMPLE_PUT_ENDPOINT = "/put/simple"
const val SIMPLE_REQUEST_BODY_PUT_ENDPOINT = "/put/request-body"
const val QUERY_PARAM_PUT_ENDPOINT = "/put/query-param"
const val SINGLE_PATH_PARAM_PUT_ENDPOINT = "/put/path/{$PATH_PARAM_1}"
const val MULTI_PATH_PARAM_PUT_ENDPOINT = "/put/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"
