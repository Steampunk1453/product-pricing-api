package com.company.product.pricing;

import com.company.product.pricing.application.usecase.GetPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

@SpringBootTest
@AutoConfigureMockMvc
class GetPriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private GetPrice getPrice;

    @Test
    @DisplayName("Case 1: Request at 10:00 on day 14th for product 35455 and brand 1 (ZARA)")
    void  shouldReturnBaseRateBeforeFirstPromotionWindow() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(1))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Case 2: Request at 16:00 on day 14th for product 35455 and brand 1 (ZARA)")
    void shouldReturnHighestPriorityRateDuringFirstPromotionWindow() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(2))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Case 3: Request at 21:00 on day 14th for product 35455 and brand 1 (ZARA)")
    void shouldReturnBaseRateAfterFirstPromotionWindow() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(1))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Case 4: Request at 10:00 on day 15th for product 35455 and brand 1 (ZARA)")
    void shouldReturnHighestPriorityRateDuringSecondPromotionWindow() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(3))
                .andExpect(jsonPath("$.startDate").value("2020-06-15T00:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-15T11:00:00"))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    @DisplayName("Case 5: Request at 21:00 on day 16th for product 35455 and brand 1 (ZARA)")
    void shouldReturnHighestPriorityRateFromSecondDayOnwards() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.applicableRate").value(4))
                .andExpect(jsonPath("$.startDate").value("2020-06-15T16:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                .andExpect(jsonPath("$.price").value(38.95));
    }

    @Test
    @DisplayName("Request with missing parameter returns BAD REQUEST 400")
    void shouldReturnBadRequestWhenRequiredParameterIsMissing() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("Request with an invalid parameter type returns BAD REQUEST 400")
    void shouldReturnBadRequestWhenParameterHasInvalidType() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-14T10:00:00")
                        .param("productId", "invalid")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Parameter 'productId' has an invalid type or format."));
    }

    @Test
    @DisplayName("Request with the identifier of a product that does not exist returns NOT FOUND 404")
    void shouldReturnNotFoundWhenNoApplicablePriceExists() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("effectiveDate", "2020-06-14T10:00:00")
                        .param("productId", "99999")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No applicable price rate found for the given request."));
    }

    @Test
    @DisplayName("Unexpected service failure returns INTERNAL SERVER ERROR 500")
    void shouldReturnInternalServerErrorWhenServiceFailsUnexpectedly() throws Exception {
        doThrow(new RuntimeException("Unexpected failure"))
                .when(getPrice)
                .execute(any(LocalDateTime.class), eq(35455), eq(1));

        try {
            mockMvc.perform(get("/api/prices")
                            .param("effectiveDate", "2020-06-14T10:00:00")
                            .param("productId", "35455")
                            .param("brandId", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status").value(500))
                    .andExpect(jsonPath("$.error").value("Internal Server Error"))
                    .andExpect(jsonPath("$.message").value("An unexpected server error occurred."));
        } finally {
            reset(getPrice);
        }
    }
}
