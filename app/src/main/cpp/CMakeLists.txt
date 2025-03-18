cmake_minimum_required(VERSION 3.10.2)
project("native-lib")

add_library(
        native-lib
        SHARED
        native_root_check.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        native-lib
        ${log-lib}
)
