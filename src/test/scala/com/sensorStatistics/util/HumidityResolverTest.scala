package com.sensorStatistics.util

import com.sensorStatistics.UnitSpec

class HumidityResolverTest extends UnitSpec {

  "resolver" should "resolve empty when NaN" in {
    HumidityResolver.resolveHumidity("NaN") shouldBe empty
  }
  "resolver" should "resolve some humidity for given number" in {
    HumidityResolver.resolveHumidity("2") shouldBe Some(2)
  }
}
