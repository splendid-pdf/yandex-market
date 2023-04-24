package com.yandex.market.auth.util;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum TestClientAttributes {
    USER(
            "33dd7324-1415-4cad-b6b8-e00804062797",
            "test@mail.ru",
            "Aa4321!8ee",
            "Bearer eyJraWQiOiJjOWUwOWQ2MS1iMzgzLTQ4ZmMtYjExMi03NTkzZDI1YmYwZTgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG1haWwucnUiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2ODIyNTYyMDYsInNjb3BlIjpbIm9wZW5pZCJdLCJ1c2VyLWlkIjoiMzNkZDczMjQtMTQxNS00Y2FkLWI2YjgtZTAwODA0MDYyNzk3IiwiaXNzIjoiaHR0cDovLzUxLjI1MC4xMDIuMTI6OTAwMCIsImV4cCI6MTcxMzc5MjIwNiwiaWF0IjoxNjgyMjU2MjA2LCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXX0.hCYX6uUjOrI5kkWcp0KCb9i-gBG1Su-cF7noRdYV6OxCwCWhJHKUEhQmj4gdcGCJSH94VVUlTDcbzAymnTQ1h4HfttrrVYMaVhBlNDR-ji1oyps101xmni8ExrfoHCMW6RXSi2HKpg7W88fiS_r-dfzY04g60dVZeHmSGoEVIhoGNAj9_K8jEz8TmhUdfq_Nh4Mn4YlJzzDk031gq_XU892elM-zkkDKPCJiFzh_tr3Wi_3MNBD1iC4r3usod4woT49PjNdbvZnc70xSrt0RbNOdxkraGXWDefEszuFsYW_5Hfp6zVlCWnq3Eaw6ulITDjqRisHNuo8tMrD3UKisEg"
    ),
    ADMIN(
            "2778a235-87f3-405d-93de-4da551145b52",
            "admin@mail.ru",
            "B5!admin",
            "Bearer eyJraWQiOiJjOWUwOWQ2MS1iMzgzLTQ4ZmMtYjExMi03NTkzZDI1YmYwZTgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLnJ1IiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjgyMjU2Nzc1LCJzY29wZSI6WyJvcGVuaWQiXSwidXNlci1pZCI6IjI3NzhhMjM1LTg3ZjMtNDA1ZC05M2RlLTRkYTU1MTE0NWI1MiIsImlzcyI6Imh0dHA6Ly81MS4yNTAuMTAyLjEyOjkwMDAiLCJleHAiOjE3MTM3OTI3NzUsImlhdCI6MTY4MjI1Njc3NSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdfQ.DRx2l8OipZemLQvG9CwviCxB9LvV1jIy4O-nOFDgvTvDspuVfuhNly9_4MZxuYjwNZDtqQLvor53ZTw-lwFDj8ygjx6afIYVndFSQa4rR99VUBYQ9ArCITThWe_8o1MaSfnVZGjgx13PYzfpA9d0Y8robtGImVdN1Vg-WMi1kfUjlH-nyV6V5tMgv1IjvU3yGIjNTNynz40GoeaBkf-4mX4Hay35gK54FhGKzDRHAAh3jBPaFlOzH8PXBeHYc8863JLBCLmI3ILKIugJadyPRVhLVIVvSYc5MwPMf2gsrF7hKtEpZNcUQVXL6VL_kpT2_n7S-izxNtHcCAwwEVW0jA"
    ),
    SELLER(
            "1ae32a42-b9a9-4e6f-a403-2adae9702ae1",
            "sellertest@gmail.ru",
            "Seller!df5",
            "Bearer eyJraWQiOiJjOWUwOWQ2MS1iMzgzLTQ4ZmMtYjExMi03NTkzZDI1YmYwZTgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzZWxsZXJ0ZXN0QGdtYWlsLnJ1IiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjgyMjU3NDY0LCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzUxLjI1MC4xMDIuMTI6OTAwMCIsImV4cCI6MTcxMzc5MzQ2NCwiaWF0IjoxNjgyMjU3NDY0LCJzZWxsZXItaWQiOiIxYWUzMmE0Mi1iOWE5LTRlNmYtYTQwMy0yYWRhZTk3MDJhZTEiLCJhdXRob3JpdGllcyI6WyJST0xFX1NFTExFUiJdfQ.Oa-Xf_WzveiVUz6RgckLLUTk2O_Ly8TfLPacDbIEoFIodFwsIvwOyhS0nuVyfsEL6MwH_1ZzEoJHS2ZwL-xKq-T3r6H267L0926XI0z_dULgb2Is6MY8dqvYWGLgJbOM3keHQEowDX4-ZZZH_DaXhDIh1D1NJ5Z9hTEYwN9640KBYj1wsEwm5iwdoAeoriRWkhLHlPYr4ZronzRid0K_eqNoGrSi9YAl_t70KPswVo7WWw8Qpxb2bN86frm_jHtbHNXqjqLFaWvx9o2yKMwGxZC6iq9guUYuVJ_Cv8_1VA7wYwZ0IdcFfMuM13Hqwt0G4Ckt4g1KMTGWkZVe_eRJ2Q"
    );

    private final String id;
    private final String email;
    private final String password;
    private final String accessToken;

    TestClientAttributes(String id, String email, String password, String accessToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }
}
