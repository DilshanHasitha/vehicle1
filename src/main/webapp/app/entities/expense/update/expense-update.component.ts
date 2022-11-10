import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ExpenseFormService, ExpenseFormGroup } from './expense-form.service';
import { IExpense } from '../expense.model';
import { ExpenseService } from '../service/expense.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  expense: IExpense | null = null;

  editForm: ExpenseFormGroup = this.expenseFormService.createExpenseFormGroup();

  constructor(
    protected expenseService: ExpenseService,
    protected expenseFormService: ExpenseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.expense = expense;
      if (expense) {
        this.updateForm(expense);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.expenseFormService.getExpense(this.editForm);
    if (expense.id !== null) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(expense: IExpense): void {
    this.expense = expense;
    this.expenseFormService.resetForm(this.editForm, expense);
  }
}
