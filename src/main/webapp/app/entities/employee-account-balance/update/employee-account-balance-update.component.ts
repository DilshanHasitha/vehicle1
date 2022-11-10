import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeAccountBalanceFormService, EmployeeAccountBalanceFormGroup } from './employee-account-balance-form.service';
import { IEmployeeAccountBalance } from '../employee-account-balance.model';
import { EmployeeAccountBalanceService } from '../service/employee-account-balance.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

@Component({
  selector: 'jhi-employee-account-balance-update',
  templateUrl: './employee-account-balance-update.component.html',
})
export class EmployeeAccountBalanceUpdateComponent implements OnInit {
  isSaving = false;
  employeeAccountBalance: IEmployeeAccountBalance | null = null;

  employeesSharedCollection: IEmployee[] = [];
  merchantsSharedCollection: IMerchant[] = [];
  transactionTypesSharedCollection: ITransactionType[] = [];

  editForm: EmployeeAccountBalanceFormGroup = this.employeeAccountBalanceFormService.createEmployeeAccountBalanceFormGroup();

  constructor(
    protected employeeAccountBalanceService: EmployeeAccountBalanceService,
    protected employeeAccountBalanceFormService: EmployeeAccountBalanceFormService,
    protected employeeService: EmployeeService,
    protected merchantService: MerchantService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareMerchant = (o1: IMerchant | null, o2: IMerchant | null): boolean => this.merchantService.compareMerchant(o1, o2);

  compareTransactionType = (o1: ITransactionType | null, o2: ITransactionType | null): boolean =>
    this.transactionTypeService.compareTransactionType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeAccountBalance }) => {
      this.employeeAccountBalance = employeeAccountBalance;
      if (employeeAccountBalance) {
        this.updateForm(employeeAccountBalance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeAccountBalance = this.employeeAccountBalanceFormService.getEmployeeAccountBalance(this.editForm);
    if (employeeAccountBalance.id !== null) {
      this.subscribeToSaveResponse(this.employeeAccountBalanceService.update(employeeAccountBalance));
    } else {
      this.subscribeToSaveResponse(this.employeeAccountBalanceService.create(employeeAccountBalance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeAccountBalance>>): void {
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

  protected updateForm(employeeAccountBalance: IEmployeeAccountBalance): void {
    this.employeeAccountBalance = employeeAccountBalance;
    this.employeeAccountBalanceFormService.resetForm(this.editForm, employeeAccountBalance);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employeeAccountBalance.employee
    );
    this.merchantsSharedCollection = this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(
      this.merchantsSharedCollection,
      employeeAccountBalance.merchant
    );
    this.transactionTypesSharedCollection = this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
      this.transactionTypesSharedCollection,
      employeeAccountBalance.transactionType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employeeAccountBalance?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.merchantService
      .query()
      .pipe(map((res: HttpResponse<IMerchant[]>) => res.body ?? []))
      .pipe(
        map((merchants: IMerchant[]) =>
          this.merchantService.addMerchantToCollectionIfMissing<IMerchant>(merchants, this.employeeAccountBalance?.merchant)
        )
      )
      .subscribe((merchants: IMerchant[]) => (this.merchantsSharedCollection = merchants));

    this.transactionTypeService
      .query()
      .pipe(map((res: HttpResponse<ITransactionType[]>) => res.body ?? []))
      .pipe(
        map((transactionTypes: ITransactionType[]) =>
          this.transactionTypeService.addTransactionTypeToCollectionIfMissing<ITransactionType>(
            transactionTypes,
            this.employeeAccountBalance?.transactionType
          )
        )
      )
      .subscribe((transactionTypes: ITransactionType[]) => (this.transactionTypesSharedCollection = transactionTypes));
  }
}
