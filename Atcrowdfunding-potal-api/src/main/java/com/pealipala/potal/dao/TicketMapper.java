package com.pealipala.potal.dao;

import com.pealipala.bean.Ticket;

public interface TicketMapper {
    Ticket getTicketByMemberId(Integer memberid);

    void saveTicket(Ticket ticket);

    void updatePstep(Ticket ticket);

    void updatePstepAndPiid(Ticket ticket);
}
