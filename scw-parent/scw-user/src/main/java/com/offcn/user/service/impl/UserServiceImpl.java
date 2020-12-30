package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TMemberMapper tMemberMapper;
    @Autowired
    private TMemberAddressMapper tMemberAddressMapper;
    @Override
    public void registerUser(TMember member) {
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(member.getLoginacct());
        long l = tMemberMapper.countByExample(example);
        if (l > 0){
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(member.getUserpswd());
        member.setUserpswd(encode);
        member.setUsername(member.getLoginacct());
        member.setEmail(member.getEmail());
        //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        member.setAuthstatus("0");
        //用户类型: 0 - 个人， 1 - 企业
        member.setUsertype("0");
        //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        member.setAccttype("2");
        System.out.println("插入数据"+member.getLoginacct());
        tMemberMapper.insertSelective(member);
    }

    @Override
    public TMember login(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(username);
        List<TMember> list = tMemberMapper.selectByExample(example);
        if (list != null && list.size() > 0){
            TMember tMember = list.get(0);
            boolean matches = encoder.matches(password, tMember.getUserpswd());
            return matches?tMember:null;
        }
        return null;
    }
    @Override
    public TMember findTmemberById(Integer id) {
        return tMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TMemberAddress> addressList(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        example.createCriteria().andMemberidEqualTo(memberId);
        return tMemberAddressMapper.selectByExample(example);
    }
}
