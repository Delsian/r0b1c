/**
 * Copyright (c) 2014 - 2017, Nordic Semiconductor ASA
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form, except as embedded into a Nordic
 *    Semiconductor ASA integrated circuit in a product or a software update for
 *    such product, must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 * 
 * 3. Neither the name of Nordic Semiconductor ASA nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * 4. This software, with or without modification, must only be used with a
 *    Nordic Semiconductor ASA integrated circuit.
 * 
 * 5. Any software provided in binary form under this license must not be reverse
 *    engineered, decompiled, modified and/or disassembled.
 * 
 * THIS SOFTWARE IS PROVIDED BY NORDIC SEMICONDUCTOR ASA "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY, NONINFRINGEMENT, AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL NORDIC SEMICONDUCTOR ASA OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
#ifndef CUSTBOARD_H
#define CUSTBOARD_H

#ifdef __cplusplus
extern "C" {
#endif

#include "nrf_gpio.h"

#define LEDS_NUMBER    0
#define BUTTONS_NUMBER 0

#define TWI0_SCL	26
#define TWI0_SDA	27

#define BUZZER_PWM		23

#define VBAT_AIN		NRF_SAADC_INPUT_AIN5

#define PWR_ON		30
#define BUTTON1     17

//== Fake define - only for compilation. Put unused numbers here
//#define BSP_LED_0 10
//#define BSP_LED_1 11
//=====

#define RX_PIN_NUMBER  11
#define TX_PIN_NUMBER  9
#define CTS_PIN_NUMBER 10
#define RTS_PIN_NUMBER 8
#define HWFC           true

#define SPIS_MISO_PIN  24    // SPI MISO signal.
#define SPIS_CSN_PIN   12    // SPI CSN signal.
#define SPIS_MOSI_PIN  25    // SPI MOSI signal.
#define SPIS_SCK_PIN   26    // SPI SCK signal.

#define SPIM0_SCK_PIN       4     /**< SPI clock GPIO pin number. */
#define SPIM0_MOSI_PIN      1     /**< SPI Master Out Slave In GPIO pin number. */
#define SPIM0_MISO_PIN      3     /**< SPI Master In Slave Out GPIO pin number. */
#define SPIM0_SS_PIN        2     /**< SPI Slave Select GPIO pin number. */

#define SPIM1_SCK_PIN       15     /**< SPI clock GPIO pin number. */
#define SPIM1_MOSI_PIN      12     /**< SPI Master Out Slave In GPIO pin number. */
#define SPIM1_MISO_PIN      14     /**< SPI Master In Slave Out GPIO pin number. */
#define SPIM1_SS_PIN        13     /**< SPI Slave Select GPIO pin number. */

// serialization APPLICATION board
#define SER_CONN_CHIP_RESET_PIN     12    // Pin used to reset connectivity chip

#define SER_APP_RX_PIN              25    // UART RX pin number.
#define SER_APP_TX_PIN              28    // UART TX pin number.
#define SER_APP_CTS_PIN             0     // UART Clear To Send pin number.
#define SER_APP_RTS_PIN             29    // UART Request To Send pin number.

#define SER_APP_SPIM0_SCK_PIN       7     // SPI clock GPIO pin number.
#define SER_APP_SPIM0_MOSI_PIN      0     // SPI Master Out Slave In GPIO pin number
#define SER_APP_SPIM0_MISO_PIN      30    // SPI Master In Slave Out GPIO pin number
#define SER_APP_SPIM0_SS_PIN        25    // SPI Slave Select GPIO pin number
#define SER_APP_SPIM0_RDY_PIN       29    // SPI READY GPIO pin number
#define SER_APP_SPIM0_REQ_PIN       28    // SPI REQUEST GPIO pin number

// Low frequency clock source to be used by the SoftDevice
#ifdef S210
#define NRF_CLOCK_LFCLKSRC      NRF_CLOCK_LFCLKSRC_XTAL_20_PPM
#else
#define NRF_CLOCK_LFCLKSRC      {.source        = NRF_CLOCK_LF_SRC_XTAL,            \
                                 .rc_ctiv       = 0,                                \
                                 .rc_temp_ctiv  = 0,                                \
                                 .xtal_accuracy = NRF_CLOCK_LF_XTAL_ACCURACY_20_PPM}
#endif

#ifdef __cplusplus
}
#endif

#endif // CUSTBOARD_H
