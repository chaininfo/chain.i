package info.chain.chaini.eosapi.remote.model.api;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import info.chain.chaini.eosapi.crypto.digest.Sha256;
import info.chain.chaini.eosapi.util.StringUtils;

/**
 * Created by swapnibble on 2018-01-10.
 */

public class GetCodeResponse {
    @Expose
    private String account_name;

    @Expose
    private String wast;

    @Expose
    private String code_hash;

    @Expose
    private JsonObject abi;

    public JsonObject getAbi() { return abi; }

    public boolean isValidCode() {
        return ! ( StringUtils.isEmpty(code_hash) || Sha256.ZERO_HASH.toString().equals( code_hash ));
    }
}
