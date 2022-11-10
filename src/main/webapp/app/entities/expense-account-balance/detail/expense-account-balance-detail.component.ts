import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseAccountBalance } from '../expense-account-balance.model';

@Component({
  selector: 'jhi-expense-account-balance-detail',
  templateUrl: './expense-account-balance-detail.component.html',
})
export class ExpenseAccountBalanceDetailComponent implements OnInit {
  expenseAccountBalance: IExpenseAccountBalance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseAccountBalance }) => {
      this.expenseAccountBalance = expenseAccountBalance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
