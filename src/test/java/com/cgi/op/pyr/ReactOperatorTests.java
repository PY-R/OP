package com.cgi.op.pyr;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

class ReactOperatorTests {

  @Test
  void monoWithConsumer() {
    Mono.just("A")
        .log()
        .doOnSubscribe(s -> System.out.println("doOnSubscribe : " + s))
        .doOnRequest(s -> System.out.println("doOnRequest : " + s))
        .doOnSuccess(s -> System.out.println("doOnSuccess : " + s))
        .subscribe(s -> System.out.println(s));
  }

  @Test
  void fluxWithConsumer() {
    Flux.fromIterable(Arrays.asList("A", "B", "C"))
        .log()
        .doOnSubscribe(s -> System.out.println("doOnSubscribe : " + s))
        .doOnRequest(s -> System.out.println("doOnRequest : " + s))
        .doOnEach(s -> System.out.println("doOnEach : " + s))
        .subscribe(s -> System.out.println(s));
  }

  @Test
  void mapTest() {
    Flux.range(1, 5).map(i -> i * 10).subscribe(s -> System.out.println(s));
  }

  @Test
  void flatMapTest() {
    Flux.range(1, 5).flatMap(i -> Flux.range(i * 10, 2)).subscribe(s -> System.out.println(s));
  }

  @Test
  void flatMapManyTest() {
    Mono.just(3).flatMapMany(i -> Flux.range(1, i)).subscribe(s -> System.out.println(s));
  }

  @Test
  void concatTest() throws InterruptedException {
    Flux<Integer> from1to5 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
    Flux<Integer> from6to10 = Flux.range(6, 5).delayElements(Duration.ofMillis(400));
    Flux.concat(from1to5, from6to10).subscribe(s -> System.out.println(s));
    Thread.sleep(4000);
  }

  @Test
  void mergeTest() throws InterruptedException {
    Flux<Integer> from1to5 = Flux.range(1, 5).delayElements(Duration.ofMillis(200));
    Flux<Integer> from6to10 = Flux.range(6, 5).delayElements(Duration.ofMillis(400));
    Flux.merge(from1to5, from6to10).subscribe(s -> System.out.println(s));
    Thread.sleep(4000);
  }

  @Test
  void zipTest() throws InterruptedException {
    Flux<Integer> from1to5 = Flux.range(1, 6);
    Flux<Integer> from6to10 = Flux.range(6, 5);
    Flux.zip(from1to5, from6to10, (v1, v2) -> v1 + ", " + v2).subscribe(s -> System.out.println(s));
    from1to5.zipWith(from6to10).subscribe(s -> System.out.println(s));
  }
}
