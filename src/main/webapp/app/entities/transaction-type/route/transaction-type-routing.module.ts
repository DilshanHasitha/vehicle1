import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionTypeComponent } from '../list/transaction-type.component';
import { TransactionTypeDetailComponent } from '../detail/transaction-type-detail.component';
import { TransactionTypeUpdateComponent } from '../update/transaction-type-update.component';
import { TransactionTypeRoutingResolveService } from './transaction-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const transactionTypeRoute: Routes = [
  {
    path: '',
    component: TransactionTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionTypeDetailComponent,
    resolve: {
      transactionType: TransactionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionTypeUpdateComponent,
    resolve: {
      transactionType: TransactionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionTypeUpdateComponent,
    resolve: {
      transactionType: TransactionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionTypeRoute)],
  exports: [RouterModule],
})
export class TransactionTypeRoutingModule {}
