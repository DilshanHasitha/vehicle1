import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICashBookBalance } from '../cash-book-balance.model';

@Component({
  selector: 'jhi-cash-book-balance-detail',
  templateUrl: './cash-book-balance-detail.component.html',
})
export class CashBookBalanceDetailComponent implements OnInit {
  cashBookBalance: ICashBookBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashBookBalance }) => {
      this.cashBookBalance = cashBookBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
