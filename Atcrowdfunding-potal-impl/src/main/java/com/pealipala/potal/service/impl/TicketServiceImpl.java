package com.pealipala.potal.service.impl;

import com.pealipala.bean.Ticket;
import com.pealipala.potal.dao.TicketMapper;
import com.pealipala.potal.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;


    public void saveTicket(Ticket ticket) {
        ticketMapper.saveTicket(ticket);
    }

    public Ticket getTicketByMemberId(Integer id) {
        return ticketMapper.getTicketByMemberId(id);
    }

    public void updatePstep(Ticket ticket) {
        ticketMapper.updatePstep(ticket);
    }

    public void updatePstepAndPiid(Ticket ticket) {
        ticketMapper.updatePstepAndPiid(ticket);
    }
}
