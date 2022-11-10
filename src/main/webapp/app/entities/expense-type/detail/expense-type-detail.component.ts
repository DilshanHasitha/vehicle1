import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseType } from '../expense-type.model';

@Component({
  selector: 'jhi-expense-type-detail',
  templateUrl: './expense-type-detail.component.html',
})
export class ExpenseTypeDetailComponent implements OnInit {
  expenseType: IExpenseType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseType }) => {
      this.expenseType = expenseType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
