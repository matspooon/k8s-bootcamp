package dev.k8s.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.k8s.backend.entity.Memo;
import dev.k8s.backend.repository.MemoRepository;

@RestController
@RequestMapping("/memo")
public class MemoController {
    private final MemoRepository memoRepository;

    public MemoController(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    // CREATE
    @PostMapping
    public Memo create(@RequestBody Memo memo) {
        return memoRepository.save(memo);
    }

    // READ(전체조회)
    @GetMapping("/findAll")
    public List<Memo> getAll() {
        return memoRepository.findAll();
    }

    // READ(단건조회)
    @GetMapping("/{id}")
    public Memo getById(Long id) {
        return memoRepository.findById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Memo update(@PathVariable Long id, @RequestBody Memo memoDetails) {
        // Memo memo = memoRepository.findById(id).orElse(null);
        // if (memo != null) {
        // memo.setTitle(memoDetails.getTitle());
        // memo.setMemo(memoDetails.getMemo());
        // return memoRepository.save(memo);
        // }
        // return null;
        return memoRepository.findById(id).map(memo -> {
            memo.setMemo(memoDetails.getMemo());
            memo.setTitle(memoDetails.getTitle());
            return memoRepository.save(memo);
        }).orElse(null);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        memoRepository.deleteById(id);
    }

}