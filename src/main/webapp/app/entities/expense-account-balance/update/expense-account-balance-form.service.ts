import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExpenseAccountBalance, NewExpenseAccountBalance } from '../expense-account-balance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseAccountBalance for edit and NewExpenseAccountBalanceFormGroupInput for create.
 */
type ExpenseAccountBalanceFormGroupInput = IExpenseAccountBalance | PartialWithRequiredKeyOf<NewExpenseAccountBalance>;

type ExpenseAccountBalanceFormDefaults = Pick<NewExpenseAccountBalance, 'id'>;

type ExpenseAccountBalanceFormGroupContent = {
  id: FormControl<IExpenseAccountBalance['id'] | NewExpenseAccountBalance['id']>;
  balance: FormControl<IExpenseAccountBalance['balance']>;
  expense: FormControl<IExpenseAccountBalance['expense']>;
  merchant: FormControl<IExpenseAccountBalance['merchant']>;
  transactionType: FormControl<IExpenseAccountBalance['transactionType']>;
};

export type ExpenseAccountBalanceFormGroup = FormGroup<ExpenseAccountBalanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseAccountBalanceFormService {
  createExpenseAccountBalanceFormGroup(
    expenseAccountBalance: ExpenseAccountBalanceFormGroupInput = { id: null }
  ): ExpenseAccountBalanceFormGroup {
    const expenseAccountBalanceRawValue = {
      ...this.getFormDefaults(),
      ...expenseAccountBalance,
    };
    return new FormGroup<ExpenseAccountBalanceFormGroupContent>({
      id: new FormControl(
        { value: expenseAccountBalanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      balance: new FormControl(expenseAccountBalanceRawValue.balance, {
        validators: [Validators.required],
      }),
      expense: new FormControl(expenseAccountBalanceRawValue.expense),
      merchant: new FormControl(expenseAccountBalanceRawValue.merchant),
      transactionType: new FormControl(expenseAccountBalanceRawValue.transactionType),
    });
  }

  getExpenseAccountBalance(form: ExpenseAccountBalanceFormGroup): IExpenseAccountBalance | NewExpenseAccountBalance {
    return form.getRawValue() as IExpenseAccountBalance | NewExpenseAccountBalance;
  }

  resetForm(form: ExpenseAccountBalanceFormGroup, expenseAccountBalance: ExpenseAccountBalanceFormGroupInput): void {
    const expenseAccountBalanceRawValue = { ...this.getFormDefaults(), ...expenseAccountBalance };
    form.reset(
      {
        ...expenseAccountBalanceRawValue,
        id: { value: expenseAccountBalanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExpenseAccountBalanceFormDefaults {
    return {
      id: null,
    };
  }
}
