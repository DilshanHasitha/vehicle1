import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ex-user',
        data: { pageTitle: 'ExUsers' },
        loadChildren: () => import('./ex-user/ex-user.module').then(m => m.ExUserModule),
      },
      {
        path: 'merchant',
        data: { pageTitle: 'Merchants' },
        loadChildren: () => import('./merchant/merchant.module').then(m => m.MerchantModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'Employees' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'employee-type',
        data: { pageTitle: 'EmployeeTypes' },
        loadChildren: () => import('./employee-type/employee-type.module').then(m => m.EmployeeTypeModule),
      },
      {
        path: 'cash-book',
        data: { pageTitle: 'CashBooks' },
        loadChildren: () => import('./cash-book/cash-book.module').then(m => m.CashBookModule),
      },
      {
        path: 'cash-book-balance',
        data: { pageTitle: 'CashBookBalances' },
        loadChildren: () => import('./cash-book-balance/cash-book-balance.module').then(m => m.CashBookBalanceModule),
      },
      {
        path: 'transaction-type',
        data: { pageTitle: 'TransactionTypes' },
        loadChildren: () => import('./transaction-type/transaction-type.module').then(m => m.TransactionTypeModule),
      },
      {
        path: 'employee-account',
        data: { pageTitle: 'EmployeeAccounts' },
        loadChildren: () => import('./employee-account/employee-account.module').then(m => m.EmployeeAccountModule),
      },
      {
        path: 'employee-account-balance',
        data: { pageTitle: 'EmployeeAccountBalances' },
        loadChildren: () => import('./employee-account-balance/employee-account-balance.module').then(m => m.EmployeeAccountBalanceModule),
      },
      {
        path: 'expense-account',
        data: { pageTitle: 'ExpenseAccounts' },
        loadChildren: () => import('./expense-account/expense-account.module').then(m => m.ExpenseAccountModule),
      },
      {
        path: 'expense-account-balance',
        data: { pageTitle: 'ExpenseAccountBalances' },
        loadChildren: () => import('./expense-account-balance/expense-account-balance.module').then(m => m.ExpenseAccountBalanceModule),
      },
      {
        path: 'expense',
        data: { pageTitle: 'Expenses' },
        loadChildren: () => import('./expense/expense.module').then(m => m.ExpenseModule),
      },
      {
        path: 'banners',
        data: { pageTitle: 'Banners' },
        loadChildren: () => import('./banners/banners.module').then(m => m.BannersModule),
      },
      {
        path: 'expense-type',
        data: { pageTitle: 'ExpenseTypes' },
        loadChildren: () => import('./expense-type/expense-type.module').then(m => m.ExpenseTypeModule),
      },
      {
        path: 'vehicle',
        data: { pageTitle: 'Vehicles' },
        loadChildren: () => import('./vehicle/vehicle.module').then(m => m.VehicleModule),
      },
      {
        path: 'images',
        data: { pageTitle: 'Images' },
        loadChildren: () => import('./images/images.module').then(m => m.ImagesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
