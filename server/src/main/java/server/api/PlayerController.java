/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lobby")
public class PlayerController {
    private List<Player> repo;

    @GetMapping(path = { "", "/" })
    public List<Player> getAll() {
        return repo;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Player> getById(@PathVariable("id") long id) {
//        if (id < 0 || !repo.existsById(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok(repo.getById(id));
//    }
//
//    @PostMapping(path = { "", "/" })
//    public ResponseEntity<Quote> add(@RequestBody Quote quote) {
//
//        if (quote.person == null || isNullOrEmpty(quote.person.firstName) || isNullOrEmpty(quote.person.lastName)
//                || isNullOrEmpty(quote.quote)) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Quote saved = repo.save(quote);
//        return ResponseEntity.ok(saved);
//    }
//
//    private static boolean isNullOrEmpty(String s) {
//        return s == null || s.isEmpty();
//    }
//
//    @GetMapping("rnd")
//    public ResponseEntity<Quote> getRandom() {
//        var idx = random.nextInt((int) repo.count());
//        return ResponseEntity.ok(repo.getById((long) idx));
//    }
}