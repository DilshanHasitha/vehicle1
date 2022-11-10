import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionTypeComponent } from './list/transaction-type.component';
import { TransactionTypeDetailComponent } from './detail/transaction-type-detail.component';
import { TransactionTypeUpdateComponent } from './update/transaction-type-update.component';
import { TransactionTypeDeleteDialogComponent } from './delete/transaction-type-delete-dialog.component';
import { TransactionTypeRoutingModule } from './route/transaction-type-routing.module';

@NgModule({
  imports: [SharedModule, TransactionTypeRoutingModule],
  declarations: [
    TransactionTypeComponent,
    TransactionTypeDetailComponent,
    TransactionTypeUpdateComponent,
    TransactionTypeDeleteDialogComponent,
  ],
})
export class TransactionTypeModule {}
