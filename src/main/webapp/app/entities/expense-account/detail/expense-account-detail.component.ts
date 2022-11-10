import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseAccount } from '../expense-account.model';

@Component({
  selector: 'jhi-expense-account-detail',
  templateUrl: './expense-account-detail.component.html',
})
export class ExpenseAccountDetailComponent implements OnInit {
  expenseAccount: IExpenseAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseAccount }) => {
      this.expenseAccount = expenseAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
