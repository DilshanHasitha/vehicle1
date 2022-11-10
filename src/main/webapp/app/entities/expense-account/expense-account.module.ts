import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpenseAccountComponent } from './list/expense-account.component';
import { ExpenseAccountDetailComponent } from './detail/expense-account-detail.component';
import { ExpenseAccountUpdateComponent } from './update/expense-account-update.component';
import { ExpenseAccountDeleteDialogComponent } from './delete/expense-account-delete-dialog.component';
import { ExpenseAccountRoutingModule } from './route/expense-account-routing.module';

@NgModule({
  imports: [SharedModule, ExpenseAccountRoutingModule],
  declarations: [
    ExpenseAccountComponent,
    ExpenseAccountDetailComponent,
    ExpenseAccountUpdateComponent,
    ExpenseAccountDeleteDialogComponent,
  ],
})
export class ExpenseAccountModule {}
