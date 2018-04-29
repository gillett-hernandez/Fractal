cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -O3 -Wall -Werror -Wno-unused-function")

add_library(fractal SHARED
            fractal.c)

# Include libraries needed for plasma lib
target_link_libraries(fractal
                      android
                      jnigraphics
                      log
                      m)
