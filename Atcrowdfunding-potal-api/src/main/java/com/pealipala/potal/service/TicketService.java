package com.pealipala.potal.service;

import com.pealipala.bean.Member;
import com.pealipala.bean.Ticket;

public interface TicketService {


    void saveTicket(Ticket ticket);

    Ticket getTicketByMemberId(Integer id);

    void updatePstep(Ticket ticket);

    void updatePstepAndPiid(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
