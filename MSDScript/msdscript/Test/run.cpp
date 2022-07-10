extern "C" {
#include "run.hpp"
};
#define CATCH_CONFIG_RUNNER
#include "/Users/angzhang/Desktop/Spring2022/6015/msdscript/msdscript/catch.h"
bool run_tests() {
 const char *argv[] = { "arith" };
 return (Catch::Session().run(1, argv) == 0);
}
