import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpenseAccountBalanceComponent } from './list/expense-account-balance.component';
import { ExpenseAccountBalanceDetailComponent } from './detail/expense-account-balance-detail.component';
import { ExpenseAccountBalanceUpdateComponent } from './update/expense-account-balance-update.component';
import { ExpenseAccountBalanceDeleteDialogComponent } from './delete/expense-account-balance-delete-dialog.component';
import { ExpenseAccountBalanceRoutingModule } from './route/expense-account-balance-routing.module';

@NgModule({
  imports: [SharedModule, ExpenseAccountBalanceRoutingModule],
  declarations: [
    ExpenseAccountBalanceComponent,
    ExpenseAccountBalanceDetailComponent,
    ExpenseAccountBalanceUpdateComponent,
    ExpenseAccountBalanceDeleteDialogComponent,
  ],
})
export class ExpenseAccountBalanceModule {}
