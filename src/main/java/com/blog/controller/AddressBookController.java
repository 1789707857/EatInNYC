package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.blog.common.BaseContext;
import com.blog.common.Result;
import com.blog.entity.AddressBook;
import com.blog.entity.User;
import com.blog.exception.CustomException;
import com.blog.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook={}", addressBook);

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        return Result.success(addressBooks);
    }

    @PostMapping
    public Result<AddressBook> addAddress(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    public Result<AddressBook> setDefaultAddress(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaUpdateWrapper<AddressBook> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(queryWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    @GetMapping("/default")
    public Result<AddressBook> defaultAddress() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return Result.success(addressBook);
    }

    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            throw new CustomException("address error");
        }
        return Result.success(addressBook);
    }

    @PutMapping
    public Result<String> updateAdd(@RequestBody AddressBook addressBook) {
        if (addressBook == null) {
            throw new CustomException("address unexist");
        }
        addressBookService.updateById(addressBook);
        return Result.success("address changed");
    }

    @DeleteMapping()
    public Result<String> deleteAdd(@RequestParam("ids") Long id) {
        if (id == null) {
            throw new CustomException("try again");
        }
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            throw new CustomException("try again");
        }
        addressBookService.removeById(id);
        return Result.success("successful");
    }
}