package com.example.alergenko.networking;

import androidx.annotation.Nullable;

public class NetworkException extends Exception {

    private int httpCode;

    public NetworkException(int httpCode) {
        super(httpCode + "");
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getDescription() {
        switch (this.httpCode) {
            case 400:
                return this.httpCode + " - Bad Request";
            case 401:
                return this.httpCode + " - Unauthorized";
            case 402:
                return this.httpCode + " - Obvezno plačilo (Payment Required)";
            case 403:
                return this.httpCode + " - Napačno uporabniško ime ali geslo (Forbidden) ";
            case 404:
                return this.httpCode + " - Not Found";
            case 405:
                return this.httpCode + " - Method Not Allowed";
            case 406:
                return this.httpCode + " - Not Acceptable";
            case 407:
                return this.httpCode + " - Proxy Authentication Required";
            case 408:
                return this.httpCode + " - Request Timeout";
            case 409:
                return this.httpCode + " - Conflict";
            case 410:
                return this.httpCode + " - Gone";
            case 411:
                return this.httpCode + " - Length Required";
            case 412:
                return this.httpCode + " - Precondition Failed";
            case 413:
                return this.httpCode + " - Payload Too Large";
            case 414:
                return this.httpCode + " - URI Too Long";
            case 415:
                return this.httpCode + " - Unsupported Media Type";
            case 416:
                return this.httpCode + " - Range Not Satisfiable";
            case 417:
                return this.httpCode + " - Expectation Failed";
            case 418:
                return this.httpCode + " - I'm a Teapot";
            case 421:
                return this.httpCode + " - Misdirected Request";
            case 422:
                return this.httpCode + " - Unprocessable Entity";
            case 423:
                return this.httpCode + " - Locked";
            case 424:
                return this.httpCode + " - Failed Dependency";
            case 425:
                return this.httpCode + " - Too Early";
            case 426:
                return this.httpCode + " - Upgrade Required";
            case 428:
                return this.httpCode + " - Precondition Required";
            case 429:
                return this.httpCode + " - Too Many Requests";
            case 431:
                return this.httpCode + " - Request Header Fields Too Large";
            case 451:
                return this.httpCode + " - Unavailable For Legal Reasons";
            case 500:
                return this.httpCode + " - Napaka na strežniku (Internal Server Error)";
            case 501:
                return this.httpCode + " - Not Implemented";
            case 502:
                return this.httpCode + " - Bad Gateway";
            case 503:
                return this.httpCode + " - Strežnik ni na voljo (Service Unavailable)";
            case 504:
                return this.httpCode + " - Gateway Timeout";
            case 505:
                return this.httpCode + " - HTTP Version Not Supported";
            case 506:
                return this.httpCode + " - Variant Also Negotiates";
            case 507:
                return this.httpCode + " - Insufficient Storage";
            case 508:
                return this.httpCode + " - Loop Detected";
            case 510:
                return this.httpCode + " - Not Extended";
            case 511:
                return this.httpCode + " - Network Authentication Required";
            default:
                return this.httpCode + " - Neznana napaka";
        }
    }

    @Nullable
    @Override
    public String getMessage() {
        return getDescription();
    }
}
