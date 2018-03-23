
#ifndef CUSTBOARD_H
#define CUSTBOARD_H

#ifdef __cplusplus
extern "C" {
#endif

#include "nrf_gpio.h"

#define LEDS_NUMBER    0
#define BUTTONS_NUMBER 0

#define TWI0_SCL		6
#define TWI0_SDA		7

#define BUZZER_PWM		8
#define PWR_ON			27
#define BUTTON1     	28
#define PIN_CHRG		29
#define PIN_STDBY		22
#define MPU_INT			20

//=== External outputs
#define PIN_EXT0		11
#define PIN_EXT1		12
#define PIN_EXT2		13
#define PIN_EXT3		14
#define PIN_EXT4		15
#define PIN_EXT5		16
#define PIN_EXT6		17
#define PIN_EXT7		19

//=== Analog inputs
#define AM0_AIN			NRF_SAADC_INPUT_AIN0
#define AM1_AIN			NRF_SAADC_INPUT_AIN1
#define AM2_AIN			NRF_SAADC_INPUT_AIN6
#define AM3_AIN			NRF_SAADC_INPUT_AIN7
#define VBAT_AIN		NRF_SAADC_INPUT_AIN2


//=== PCA config ===
#define PCA9685_LEDR				14
#define PCA9685_LEDG				13
#define PCA9685_LEDB				15

#define PCA9685_PWMA				0
#define PCA9685_IN1A				2
#define PCA9685_IN2A				1

#define PCA9685_PWMB				5
#define PCA9685_IN1B				3
#define PCA9685_IN2B				4

#define PCA9685_PWMC				6
#define PCA9685_IN1C				8
#define PCA9685_IN2C				7

#define PCA9685_PWMD				11
#define PCA9685_IN1D				9
#define PCA9685_IN2D				10

//===== Not used

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

#ifdef __cplusplus
}
#endif

#endif // CUSTBOARD_H
