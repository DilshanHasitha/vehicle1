import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ExpenseAccountFormService, ExpenseAccountFormGroup } from './expense-account-form.service';
import { IExpenseAccount } from '../expense-account.model';
import { ExpenseAccountService } from '../service/expense-account.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { IExpense } from 'app/entities/expense/expense.model';
import { ExpenseService } from 'app/entities/expense/service/expense.service';

@Component({
  selector: 'jhi-expense-account-update',
  templateUrl: './expense-account-update.component.html',
})
export class ExpenseAccountUpdateComponent implements OnInit {
  isSaving = false;
  expenseAccount: IExpenseAccount | null = null;

  transactionTypesSharedCollection: ITransactionType[] = [];
  merchantsSharedCollection: IMerchant[] = [];
  expensesSharedCollection: IExpense[] = [];

  editForm: ExpenseAccountFormGroup = this.expenseAccountFormService.createExpenseAccountFormGroup();

  constructor(
    protected expenseAccountService: ExpenseAccountService,
    protected expenseAccountFormService: ExpenseAccountFormService,
    protected transactionTypeService: TransactionTypeService,
    protected merchantService: MerchantService,
    protected expenseService: ExpenseService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareExpense = (o1: IExpense | null, o2: IExpense | null): boolean => this.expenseService.compareExpense(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseAccount }) => {
      this.expenseAccount = expenseAccount;
      if (expenseAccount) {
        this.updateForm(expenseAccount);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseAccount = this.expenseAccountFormService.getExpenseAccount(this.editForm);
    if (expenseAccount.id !== null) {
      this.subscribeToSaveResponse(this.expenseAccountService.update(expenseAccount));
    } else {
      this.subscribeToSaveResponse(this.expenseAccountService.create(expenseAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseAccount>>): void {
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

  protected updateForm(expenseAccount: IExpenseAccount): void {
    this.expenseAccount = expenseAccount;
    this.expenseAccountFormService.resetForm(this.editForm, expenseAccount);

    this.transactionTypesSharedCollection = this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
      this.transactionTypesSharedCollection,
      expenseAccount.transactionType
    );
    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      expenseAccount.merchant
    );
    this.expensesSharedCollection = this.expenseService.addExpenseToCollectionIfMissing<IExpense>(
      this.expensesSharedCollection,
      expenseAccount.expense
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionTypeService
      .query()
      .pipe(map((res: HttpResponse<ITransactionType[]>) => res.body ?? []))
      .pipe(
        map((transactionTypes: ITransactionType[]) =>
          this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
            transactionTypes,
            this.expenseAccount?.transactionType
          )
        )
      )
      .subscribe((transactionTypes: ITransactionType[]) => (this.transactionTypesSharedCollection = transactionTypes));

    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) =>
          this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.expenseAccount?.merchant)
        )
      )
      .subscribe((merchants: IMerchant[]) => (this.merchantsSharedCollection = merchants));

    this.expenseService
      .query()
      .pipe(map((res: HttpResponse<IExpense[]>) => res.body ?? []))
      .pipe(
        map((expenses: IExpense[]) => this.expenseService.addExpenseToCollectionIfMissing<IExpense>(expenses, this.expenseAccount?.expense))
      )
      .subscribe((expenses: IExpense[]) => (this.expensesSharedCollection = expenses));
  }
}
